import { useEffect, useState } from "react";
import api from "../api";

function LoginPage() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // ✅ Auto redirect if already logged in
  useEffect(() => {

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (token && role === "ADMIN") {

      window.location.href = "/admin";
    }

    if (token && role === "USER") {

      window.location.href = "/user";
    }

  }, []);

  const handleLogin = async () => {

    try {

      const response = await api.post("/auth/login", {
        username,
        password,
      });

      const body = response.data.data || response.data;
      const token = body.token;
      const role = body.role;

      localStorage.setItem("token", token);
      localStorage.setItem("role", role);

      alert("Login Success");

      if (role === "ADMIN") {

        window.location.href = "/admin";

      } else {

        window.location.href = "/user";
      }

    } catch (error) {

      console.log(error);

      alert("Login Failed");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h2 className="form-title">Welcome Back</h2>
        <p className="form-subtitle">
          Sign in to manage access requests and review governance activity.
        </p>

        <div className="form-field">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="form-input"
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="form-input"
          />

          <button onClick={handleLogin} className="primary-button" type="button">
            Login
          </button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;