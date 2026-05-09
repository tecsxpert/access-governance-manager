import { useState } from "react";

import axios from "axios";

import { toast } from "react-toastify";

function ResetPassword() {

  const [username,
    setUsername] =
    useState("");

  const [password,
    setPassword] =
    useState("");

  const handleReset =
    async () => {

      try {

        await axios.post(
          "http://localhost:8080/auth/reset-password",
          {
            username,
            password,
          }
        );

        toast.success(
          "Password Reset Successful"
        );

      } catch (error) {

        console.log(error);

        toast.error(
          error.response?.data?.message
          || "Reset failed"
        );
      }
    };

  return (

    <div className="auth-container">

      <div className="auth-card">

        <h2>
          Reset Password
        </h2>

        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) =>
            setUsername(
              e.target.value
            )
          }
          className="form-input"
        />

        <input
          type="password"
          placeholder="New Password"
          value={password}
          onChange={(e) =>
            setPassword(
              e.target.value
            )
          }
          className="form-input"
        />

        <button
          onClick={handleReset}
          className="primary-button"
        >
          Reset Password
        </button>

      </div>

    </div>
  );
}

export default ResetPassword;