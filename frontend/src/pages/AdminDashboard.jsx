import { useNavigate } from "react-router-dom";

function AdminDashboard() {

  const navigate = useNavigate();

  const role = localStorage.getItem("role");

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");

    navigate("/");
  };

  return (
    <div style={{ textAlign: "center", marginTop: "100px" }}>

      <h1>Admin Dashboard</h1>

      <h3>Welcome {role}</h3>

      <div style={{ marginTop: "30px" }}>

        <button
          style={{
            padding: "10px 20px",
            marginRight: "10px",
            cursor: "pointer"
          }}
        >
          View Requests
        </button>

        <button
          onClick={logout}
          style={{
            padding: "10px 20px",
            backgroundColor: "red",
            color: "white",
            border: "none",
            cursor: "pointer"
          }}
        >
          Logout
        </button>

      </div>

    </div>
  );
}

export default AdminDashboard;