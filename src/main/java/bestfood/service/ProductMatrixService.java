package bestfood.service;

import bestfood.model.ProductMatrix;
import bestfood.model.ProductPair;
import bestfood.model.Product;
import bestfood.repo.ProductMatrixRepo;
import bestfood.repo.ProductRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductMatrixService {

    private final ProductMatrixRepo productMatrixRepo;
    private final ProductRepo productRepo;

    public ProductMatrixService(ProductMatrixRepo productMatrixRepo, ProductRepo productRepo) {

        this.productMatrixRepo = productMatrixRepo;
        this.productRepo = productRepo;
    }

    public ProductMatrix getProductMatrix(int productId) {

        Product product = productRepo.findById(productId).orElse(null);

        if (product == null) {
            return null;
        }

        return productMatrixRepo.findByProduct(product);
    }

    public void updateProductMatrixPair(int productId1, int productId2, int count) {

        Product product1 = productRepo.findById(productId1).orElse(null);
        Product product2 = productRepo.findById(productId2).orElse(null);

        if (product1 == null || product2 == null) {
            return;
        }

        ProductMatrix matrix = productMatrixRepo.findByProduct(product1);

        if (matrix == null) {

            matrix = new ProductMatrix(product1);

        }

        matrix.setPairCount(product2, count);

        productMatrixRepo.save(matrix);
    }

    public ProductPair getMaxPairForProduct(int productId) {

        ProductMatrix matrix = getProductMatrix(productId);

        if (matrix == null || matrix.getProductPairs().isEmpty()) {
            return null;
        }

        return matrix.getProductPairs()
            .stream().max((a, b) -> Integer.compare(a.getCount(), b.getCount()))
            .orElse(null);
    }

    public List<ProductMatrix> getAllProductMatrices() {

        return productMatrixRepo.findAll();
    }

    public void deleteProductReferences(Product product) {

        List<ProductMatrix> matrices = productMatrixRepo.findAll();

        for (ProductMatrix matrix : matrices) {

            matrix.getProductPairs().removeIf(pair -> pair.getPairedProduct().getId().equals(product.getId()));

            if (matrix.getProduct().getId().equals(product.getId())) {

                productMatrixRepo.delete(matrix);

            } else {
                productMatrixRepo.save(matrix);
            }
        }
    }

    public void updateProductPair(int productId, int pairId) {
        Product product = productRepo.findById(productId).orElse(null);

        if (product != null) {
            product.setProductPair(pairId);
            productRepo.save(product);
        }
    }

    public void updateProductPairs() {

        List<ProductMatrix> matrices = getAllProductMatrices();
        for (ProductMatrix matrix : matrices) {

            ProductPair maxPair = getMaxPairForProduct(matrix.getProduct().getId());

            if (maxPair != null) {

                updateProductPair(matrix.getProduct().getId(), maxPair.getPairedProduct().getId());
            }
        }
    }

}