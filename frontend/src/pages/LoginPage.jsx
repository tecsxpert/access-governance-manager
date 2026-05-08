import { useState } from "react";
import axios from "axios";

function LoginPage() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {

    try {

      const response = await axios.post(
        "http://localhost:8080/auth/login",
        {
          username,
          password,
        }
      );

      console.log(response.data);

      // ✅ backend returns:
      // response.data.data.token
      // response.data.data.role

      const token = response.data.data.token;
      const role = response.data.data.role;

      // ✅ save in localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);

      alert("Login Success");

      // ✅ redirect based on role
      if (role === "ADMIN" || role === "ROLE_ADMIN") {

        window.location.href = "/admin";

      } else {

        window.location.href = "/user";
      }

    } catch (error) {

      console.error(error);

      alert("Login Failed");
    }
  };

  return (

    <div
      style={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#f4f4f4",
      }}
    >

      <div
        style={{
          display: "flex",
          flexDirection: "column",
          width: "350px",
          gap: "15px",
          backgroundColor: "white",
          padding: "40px",
          borderRadius: "10px",
          boxShadow: "0px 0px 10px rgba(0,0,0,0.2)",
        }}
      >

        <h2 style={{ textAlign: "center" }}>
          Access Governance Manager
        </h2>

        <input
          type="text"
          placeholder="Enter Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          style={{
            padding: "12px",
            borderRadius: "5px",
            border: "1px solid gray",
          }}
        />

        <input
          type="password"
          placeholder="Enter Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={{
            padding: "12px",
            borderRadius: "5px",
            border: "1px solid gray",
          }}
        />

        <button
          onClick={handleLogin}
          style={{
            padding: "12px",
            border: "none",
            borderRadius: "5px",
            backgroundColor: "#4CAF50",
            color: "white",
            cursor: "pointer",
            fontSize: "16px",
          }}
        >
          Login
        </button>

      </div>

    </div>
  );
}

export default LoginPage;