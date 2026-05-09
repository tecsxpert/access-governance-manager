import {
  useEffect,
  useState,
} from "react";

import {
  toast,
} from "react-toastify";

import {
  Link,
} from "react-router-dom";

import api from "../api";

function LoginPage() {

  const [username,
    setUsername] =
    useState("");

  const [password,
    setPassword] =
    useState("");

  const [loading,
    setLoading] =
    useState(false);

  // ✅ AUTO REDIRECT
  useEffect(() => {

    const token =
      localStorage.getItem("token");

    const role =
      localStorage.getItem("role");

    if (token && role === "ADMIN") {

      window.location.href =
        "/admin";
    }

    if (token && role === "USER") {

      window.location.href =
        "/user";
    }

  }, []);

  // ✅ LOGIN
  const handleLogin = async () => {

    if (!username || !password) {

      toast.error(
        "Please enter username and password"
      );

      return;
    }

    try {

      setLoading(true);

      const response =
        await api.post(
          "/auth/login",
          {
            username,
            password,
          }
        );

      console.log(response.data);

      const body =
        response.data.data
        || response.data;

      const token =
        body.token;

      const role =
        body.role;

      localStorage.setItem(
        "token",
        token
      );

      localStorage.setItem(
        "role",
        role
      );

      toast.success(
        "Login Success"
      );

      setTimeout(() => {

        if (role === "ADMIN") {

          window.location.href =
            "/admin";

        } else {

          window.location.href =
            "/user";
        }

      }, 1000);

    } catch (error) {

      console.log(error);

      toast.error(
        error.response?.data?.message
        || "Login Failed"
      );

    } finally {

      setLoading(false);
    }
  };
    
  // ✅ ENTER KEY LOGIN
  const handleKeyPress = (e) => {

    if (e.key === "Enter") {

      handleLogin();
    }
  };

  return (

    <div className="auth-page">

      <div className="auth-card">

        <h1 className="form-title">
          ACCESS GOVERNANCE MANAGER
        </h1>

        <p className="form-subtitle">

          Sign in to manage
          access requests,
          approvals,
          analytics,
          and governance.

        </p>

        <div className="form-field">

          <input
            type="text"
            placeholder="Enter Username"
            value={username}
            onChange={(e) =>
              setUsername(
                e.target.value
              )
            }
            onKeyDown={handleKeyPress}
            className="form-input"
          />

          <input
            type="password"
            placeholder="Enter Password"
            value={password}
            onChange={(e) =>
              setPassword(
                e.target.value
              )
            }
            onKeyDown={handleKeyPress}
            className="form-input"
          />

          <button
            onClick={handleLogin}
            className="primary-button"
            type="button"
            disabled={loading}
          >

            {loading
              ? "Logging in..."
              : "Login"}

          </button>

          {/* ✅ FORGOT PASSWORD */}
          <div
            style={{
              textAlign: "center",
              marginTop: "10px",
            }}
          >

            <Link
              to="/forgot-password"
              style={{
                textDecoration: "none",
                color: "#2563eb",
                fontWeight: "bold",
              }}
            >
              Forgot Password?
            </Link>

          </div>

        </div>

      </div>

    </div>
  );
}

export default LoginPage;