import { useEffect, useState } from "react";
import { Package, RefreshCw, Plus } from "lucide-react";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";

export default function Products() {
  const { user } = useAuth();

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const isAdmin = user?.role === "ADMIN";

  async function loadProducts() {
    setLoading(true);
    setError("");

    try {
      const response = await api.get("/api/products");
      setProducts(response.data);
    } catch (err) {
      setError(
        err.response?.status === 401
          ? "Your session has expired. Please log in again."
          : "Unable to load products. Check your backend connection."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadProducts();
  }, []);

  function formatPrice(price) {
    return Number(price).toFixed(2);
  }

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Products</h1>
          <p>Manage your engineering product catalog.</p>
        </div>

        <div className="page-actions">
          <button className="secondary-btn" onClick={loadProducts}>
            <RefreshCw size={17} />
            Refresh
          </button>

          {isAdmin && (
            <button
              className="action-btn"
              onClick={() =>
                window.alert("Add Product form is coming in Task 3.")
              }
            >
              <Plus size={17} />
              Add Product
            </button>
          )}
        </div>
      </div>

      {loading && <p>Loading products...</p>}

      {error && <div className="error-message">{error}</div>}

      {!loading && !error && products.length === 0 && (
        <div className="empty-state">
          <Package size={36} />
          <h3>No products found</h3>
          <p>Add products using the backend API.</p>
        </div>
      )}

      {!loading && !error && products.length > 0 && (
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>SKU</th>
                <th>Product Name</th>
                <th>Description</th>
                <th>Base Price</th>
              </tr>
            </thead>

            <tbody>
              {products.map((product) => (
                <tr key={product.id}>
                  <td>{product.id}</td>
                  <td>
                    <span className="sku-label">{product.sku}</span>
                  </td>
                  <td className="product-name">{product.name}</td>
                  <td>{product.description || "—"}</td>
                  <td>{formatPrice(product.basePrice)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}