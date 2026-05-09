import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";

import LoginPage from "./pages/LoginPage";
import UserDashboard from "./pages/UserDashboard";
import AdminDashboard from "./pages/AdminDashboard";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {

  return (

    <BrowserRouter>

      <Routes>

        {/* ✅ LOGIN */}
        <Route
          path="/"
          element={<LoginPage />}
        />

        {/* ✅ USER */}
        <Route
          path="/user"
          element={
            <ProtectedRoute role="USER">

              <UserDashboard />

            </ProtectedRoute>
          }
        />

        {/* ✅ ADMIN */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute role="ADMIN">

              <AdminDashboard />

            </ProtectedRoute>
          }
        />

      </Routes>
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop={false} closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover theme="colored" />
    </BrowserRouter>
  );
}

export default App;