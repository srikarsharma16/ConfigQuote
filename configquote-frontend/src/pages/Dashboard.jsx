
import { Link } from "react-router-dom";
import { Package, Users, FileText, ArrowRight } from "lucide-react";
import { useAuth } from "../context/AuthContext";

export default function Dashboard() {
  const { user } = useAuth();

  const sections = [
    {
      title: "Products",
      description: "Browse and manage engineering products.",
      path: "/products",
      icon: Package,
    },
    {
      title: "Customers",
      description: "View and manage customer information.",
      path: "/customers",
      icon: Users,
    },
    {
      title: "Quotations",
      description: "Create and track customer quotations.",
      path: "/quotations",
      icon: FileText,
    },
  ];

  return (
    <div>
      <div className="page-heading">
        <div>
          <h1>Dashboard</h1>
          <p>
            Welcome back, <strong>{user?.username}</strong>.
          </p>
        </div>
      </div>

      <div className="overview-grid">
        {sections.map(({ title, description, path, icon: Icon }) => (
          <Link to={path} className="overview-card" key={path}>
            <Icon size={28} />
            <h3>{title}</h3>
            <p>{description}</p>
            <span>
              Open section <ArrowRight size={16} />
            </span>
          </Link>
        ))}
      </div>
    </div>
  );
}