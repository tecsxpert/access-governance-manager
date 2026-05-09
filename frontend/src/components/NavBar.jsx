import { useTheme } from "../context/ThemeContext";

function Navbar() {

  const {
    darkMode,
    toggleTheme,
  } = useTheme();

  const role =
    localStorage.getItem("role");

  // ✅ LOGOUT
  const logout = () => {

    localStorage.removeItem(
      "token"
    );

    localStorage.removeItem(
      "role"
    );

    // ✅ FIXED BLANK SCREEN ISSUE
    window.location.href = "/";
  };

  return (

    <div
      className={`app-navbar ${
        darkMode ? "dark" : ""
      }`}
    >

      <h2>
        Access Governance Manager
      </h2>

      <div className="navbar-actions">

        <span
          className={`role-badge role-${role?.toLowerCase()}`}
        >
          {role}
        </span>

        <button
          onClick={toggleTheme}
          className="secondary-button"
          type="button"
        >

          {darkMode
            ? "Light"
            : "Dark"}

        </button>

        <button
          onClick={logout}
          className="secondary-button"
          type="button"
        >
          Logout
        </button>

      </div>

    </div>
  );
}

export default Navbar;