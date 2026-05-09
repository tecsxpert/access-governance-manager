import { useState } from "react";

import axios from "axios";

import { toast } from "react-toastify";

function ForgotPassword() {

  const [username,
    setUsername] =
    useState("");

  const handleForgotPassword =
    async () => {

      try {

        await axios.post(
          "http://localhost:8080/auth/forgot-password",
          {
            username,
          }
        );

        toast.success(
          "Reset link sent to email"
        );

      } catch (error) {

        console.log(error);

        toast.error(
          error.response?.data?.message
          || "Something went wrong"
        );
      }
    };

  return (

    <div className="auth-container">

      <div className="auth-card">

        <h2>
          Forgot Password
        </h2>

        <input
          type="text"
          placeholder="Enter Username"
          value={username}
          onChange={(e) =>
            setUsername(
              e.target.value
            )
          }
          className="form-input"
        />

        <button
          onClick={
            handleForgotPassword
          }
          className="primary-button"
        >
          Send Reset Link
        </button>

      </div>

    </div>
  );
}

export default ForgotPassword;