import { Navigate } from "react-router-dom";

function ProtectedRoute({ children, role }) {

  const token = localStorage.getItem("token");
  const storedRole = localStorage.getItem("role");

  // ✅ No token
  if (!token) {
    return <Navigate to="/" />;
  }

  // ✅ Wrong role
  if (role && storedRole !== role) {
    return <Navigate to="/" />;
  }

  return children;
}

export default ProtectedRoute;