package bestfood.service;

import bestfood.model.Product;
import bestfood.model.ProductLink;
import bestfood.repo.ProductLinkRepo;
import bestfood.repo.ProductRepo;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class ProductLinkService {

    private final ProductLinkRepo productLinkRepo;
    private final ProductRepo productRepo;

    public ProductLinkService(ProductLinkRepo productLinkRepo, ProductRepo productRepo) {

        this.productLinkRepo = productLinkRepo;
        this.productRepo = productRepo;
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

    public void updateBoughtWithProductsToBest(Set<Integer> originalProductIds) {

        for (Integer productId : originalProductIds) {

            updateBoughtWithProductToBest(productId);
        }
    }

}