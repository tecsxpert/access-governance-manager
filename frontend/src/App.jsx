import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import {
  ToastContainer,
} from "react-toastify";

import "react-toastify/dist/ReactToastify.css";

import LoginPage from "./pages/LoginPage";

import UserDashboard from "./pages/UserDashboard";

import AdminDashboard from "./pages/AdminDashboard";

import ProtectedRoute from "./components/ProtectedRoute";

import ForgotPassword from "./pages/ForgotPassword";

import ResetPassword from "./pages/ResetPassword";


function App() {

  const token =
    localStorage.getItem("token");

  const role =
    localStorage.getItem("role");

  return (

    <BrowserRouter>

      <Routes>

        {/* ✅ LOGIN */}
        <Route
          path="/"
          element={

            token ? (

              role === "ADMIN"

                ? <Navigate to="/admin" />

                : <Navigate to="/user" />

            ) : (

              <LoginPage />

            )
          }
        />

        {/* ✅ USER DASHBOARD */}
        <Route
          path="/user"
          element={

            <ProtectedRoute role="USER">

              <UserDashboard />

            </ProtectedRoute>

          }
        />

        {/* ✅ ADMIN DASHBOARD */}
        <Route
          path="/admin"
          element={

            <ProtectedRoute role="ADMIN">

              <AdminDashboard />

            </ProtectedRoute>

          }
        />
      
        {/* ✅ FORGOT PASSWORD */}
        <Route
          path="/forgot-password"
          element={
            <ForgotPassword />
          }
        />

        {/* ✅ RESET PASSWORD */}
        <Route
          path="/reset-password"
          element={
            <ResetPassword />
          }
        />

      </Routes>

      {/* ✅ TOAST */}
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
      />

    </BrowserRouter>
  );
}

export default App;