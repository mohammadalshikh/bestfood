package bestfood.service;

import bestfood.model.Product;
import bestfood.model.ProductLink;
import bestfood.repo.ProductLinkRepo;
import bestfood.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductLinkService {

    private final ProductLinkRepo productLinkRepo;
    private final ProductRepo productRepo;

    public ProductLinkService(ProductLinkRepo productLinkRepo, ProductRepo productRepo) {

        this.productLinkRepo = productLinkRepo;
        this.productRepo = productRepo;
    }

    public void deleteAllLinksForProduct(Integer productId) {

        List<ProductLink> links = productLinkRepo.findAll();

        for (ProductLink link : links) {

            if (link.getOriginalProduct().getId().equals(productId)
                || link.getBoughtWithProduct().getId().equals(productId)) {

                productLinkRepo.delete(link);
            }
        }
    }

    public Set<Integer> getAffectedProductIds(Integer productId) {

        Set<Integer> affectedProductIds = new HashSet<>();

        List<ProductLink> links = productLinkRepo.findByOriginalProductIdOrBoughtWithProductId(productId, productId);

        for (ProductLink link : links) {

            if (link.getOriginalProduct().getId().equals(productId)) {

                affectedProductIds.add(link.getBoughtWithProduct().getId());
            }

            if (link.getBoughtWithProduct().getId().equals(productId)) {

                affectedProductIds.add(link.getOriginalProduct().getId());
            }
        }

        return affectedProductIds;
    }

    public ProductLink findBestBoughtWith(Integer originalProductId) {

        Product product = productRepo.findById(originalProductId).orElse(null);

        if (product == null) {
            return null;
        }

        return productLinkRepo.findTopByOriginalProductOrderByPurchaseCountDesc(product);
    }

    public void updateBoughtWithProductToX(Integer originalProductId, Integer boughtWithProductId) {

        Product product = productRepo.findById(originalProductId).orElse(null);

        if (product == null) {
            return;
        }

        Product boughtWithProduct = null;

        if (boughtWithProductId != null) {

            boughtWithProduct = productRepo.findById(boughtWithProductId).orElse(null);
        }

        product.setBoughtWithProduct(boughtWithProduct);
        productRepo.save(product);
    }

    private void updateBoughtWithProductToBest(Integer productId) {

        ProductLink bestLink = findBestBoughtWith(productId);

        if (bestLink == null) {

            updateBoughtWithProductToX(productId, null);

        } else {

            updateBoughtWithProductToX(productId, bestLink.getBoughtWithProduct().getId());
        }
    }

    public void incrementProductLinkCount(Integer originalProductId, Integer boughtWithProductId) {

        Product originalProduct = productRepo.findById(originalProductId).orElse(null);
        Product boughtWithProduct = productRepo.findById(boughtWithProductId).orElse(null);

        if (originalProduct == null || boughtWithProduct == null) {
            return;
        }

        ProductLink link = productLinkRepo.findByOriginalProductAndBoughtWithProduct(originalProduct, boughtWithProduct);

        if (link == null) {

            link = new ProductLink(originalProduct, boughtWithProduct, 1);

        } else {

            link.setPurchaseCount(link.getPurchaseCount() + 1);
        }

        productLinkRepo.save(link);
    }

    public void updateSomeBoughtWithProductsToBest(Set<Integer> productIds) {

        for (Integer productId : productIds) {

            updateBoughtWithProductToBest(productId);
        }
    }

    public void updateAllBoughtWithProductsToBest() {

        List<ProductLink> links = productLinkRepo.findAll();

        Set<Integer> productIds = new HashSet<>();

        for (ProductLink link : links) {

            productIds.add(link.getOriginalProduct().getId());
        }

        for (Integer productId : productIds) {

            updateBoughtWithProductToBest(productId);
        }
    }

}