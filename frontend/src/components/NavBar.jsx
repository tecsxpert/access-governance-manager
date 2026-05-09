import { useNavigate } from "react-router-dom";

function Navbar() {

  const navigate = useNavigate();

  const role = localStorage.getItem("role");

  const logout = () => {

    localStorage.removeItem("token");
    localStorage.removeItem("role");

    navigate("/");
  };

  return (
    <div className="app-navbar">
      <h2>Access Governance Manager</h2>
      <div className="navbar-actions">
        <span className={`role-badge role-${role?.toLowerCase()}`}>{role}</span>
        <button onClick={logout} className="secondary-button" type="button">
          Logout
        </button>
      </div>
    </div>
  );
}

export default Navbar;