import React, {
  useState,
  useEffect,
  useMemo,
} from "react";

import axios from "axios";

import {
  toast,
} from "react-toastify";

import "react-toastify/dist/ReactToastify.css";

import NavBar from "../components/NavBar";

import StatusBadge from "../components/StatusBadge";

import RequestModal from "../components/RequestModal";

import {
  useTheme,
} from "../context/ThemeContext";

import AnalyticsChart from "../components/AnalyticsChart";

import ProfileCard from "../components/ProfileCard";

import {
  exportToCSV,
} from "../utils/exportCSV";

function AdminDashboard() {

  const { darkMode } =
    useTheme();

  const [requests,
    setRequests] = useState([]);

  const [loading,
    setLoading] = useState(true);

  const [searchTerm,
    setSearchTerm] = useState("");

  const [statusFilter,
    setStatusFilter] =
    useState("ALL");

  const [currentPage,
    setCurrentPage] =
    useState(1);

  const [selectedRequest,
    setSelectedRequest] =
    useState(null);

  const itemsPerPage = 10;

  const role =
    localStorage.getItem("role")
    || "ADMIN";

  useEffect(() => {

    setCurrentPage(1);

  }, [searchTerm, statusFilter]);

  useEffect(() => {

    fetchRequests();

  }, []);

  // ✅ FETCH REQUESTS
  const fetchRequests = async () => {

    try {

      setLoading(true);

      const token =
        localStorage.getItem("token");

      const response =
        await axios.get(
          "http://localhost:8080/access/all",
          {
            headers: {
              Authorization:
                `Bearer ${token}`,
            },
          }
        );

      setRequests(
        response.data.data || []
      );

    } catch (error) {

      console.log(error);

      toast.error(
        error.response?.data?.message
        || "Failed to load requests"
      );

    } finally {

      setLoading(false);
    }
  };

  // ✅ APPROVE
  const approveRequest = async (
    id
  ) => {

    try {

      const token =
        localStorage.getItem("token");

      await axios.put(
        `http://localhost:8080/access/approve/${id}`,
        {},
        {
          headers: {
            Authorization:
              `Bearer ${token}`,
          },
        }
      );

      toast.success(
        "Request Approved & Email Sent"
      );

      fetchRequests();

    } catch (error) {

      console.log(error);

      toast.error(
        error.response?.data?.message
        || "Approval failed"
      );
    }
  };

  // ✅ REJECT
  const rejectRequest = async (
    id
  ) => {

    try {

      const token =
        localStorage.getItem("token");

      await axios.put(
        `http://localhost:8080/access/reject/${id}`,
        {},
        {
          headers: {
            Authorization:
              `Bearer ${token}`,
          },
        }
      );

      toast.error(
        "Request Rejected & Email Sent"
      );

      fetchRequests();

    } catch (error) {

      console.log(error);

      toast.error(
        error.response?.data?.message
        || "Rejection failed"
      );
    }
  };

  // ✅ FILTER
  const filteredRequests =
    useMemo(() => {

      return requests.filter(
        (request) => {

          const matchesSearch =

            request.userName
              ?.toLowerCase()
              .includes(
                searchTerm.toLowerCase()
              )

            ||

            request.resourceName
              ?.toLowerCase()
              .includes(
                searchTerm.toLowerCase()
              )

            ||

            request.accessType
              ?.toLowerCase()
              .includes(
                searchTerm.toLowerCase()
              );

          const matchesStatus =

            statusFilter === "ALL"

            ||

            request.status ===
            statusFilter;

          return (
            matchesSearch
            && matchesStatus
          );
        }
      );

    }, [
      requests,
      searchTerm,
      statusFilter,
    ]);

  // ✅ PAGINATION
  const totalPages =
    Math.ceil(
      filteredRequests.length
      / itemsPerPage
    );

  const currentRequests =
    filteredRequests.slice(
      (currentPage - 1)
      * itemsPerPage,

      currentPage
      * itemsPerPage
    );

  // ✅ ANALYTICS
  const totalCount =
    requests.length;

  const pendingCount =
    requests.filter(
      (request) =>
        request.status ===
        "PENDING"
    ).length;

  const approvedCount =
    requests.filter(
      (request) =>
        request.status ===
        "APPROVED"
    ).length;

  const rejectedCount =
    requests.filter(
      (request) =>
        request.status ===
        "REJECTED"
    ).length;

  return (

    <>

      <NavBar />

      <div
        className={`dashboard-layout ${
          darkMode ? "dark" : ""
        }`}
      >

        {/* ✅ HEADER */}
        <div>

          <h1>
            Admin Dashboard
          </h1>

          <p>
            Manage requests,
            approvals,
            analytics,
            and governance.
          </p>

        </div>

        {/* ✅ ROLE CARD */}
        <div className="info-card">

          <h3>
            Current Role
          </h3>

          <p>
            {role}
          </p>

        </div>

        {/* ✅ PROFILE */}
        <ProfileCard />

        {/* ✅ EXPORT */}
        <button
          onClick={() =>
            exportToCSV(requests)
          }
          style={{
            padding: "10px",
            marginBottom: "20px",
            cursor: "pointer",
          }}
        >
          Export CSV
        </button>

        {/* ✅ STATS */}
        <div className="dashboard-cards">

          <div className="info-card">

            <h3>
              Total Requests
            </h3>

            <p>
              {totalCount}
            </p>

          </div>

          <div className="info-card">

            <h3>
              Pending
            </h3>

            <p>
              {pendingCount}
            </p>

          </div>

          <div className="info-card">

            <h3>
              Approved
            </h3>

            <p>
              {approvedCount}
            </p>

          </div>

          <div className="info-card">

            <h3>
              Rejected
            </h3>

            <p>
              {rejectedCount}
            </p>

          </div>

        </div>

        {/* ✅ ANALYTICS CHART */}
        <AnalyticsChart
          pendingCount={pendingCount}
          approvedCount={approvedCount}
          rejectedCount={rejectedCount}
        />

        {/* ✅ SEARCH + FILTER */}
        <div className="dashboard-controls">

          <input
            type="text"
            placeholder="Search by user, resource or access type..."
            value={searchTerm}
            onChange={(e) =>
              setSearchTerm(
                e.target.value
              )
            }
            className="form-input"
          />

          <select
            value={statusFilter}
            onChange={(e) =>
              setStatusFilter(
                e.target.value
              )
            }
            className="form-input"
          >

            <option value="ALL">
              All Statuses
            </option>

            <option value="PENDING">
              Pending
            </option>

            <option value="APPROVED">
              Approved
            </option>

            <option value="REJECTED">
              Rejected
            </option>

          </select>

        </div>

        {/* ✅ TABLE */}
        <div className="table-card">

          <table className="data-table">

            <thead>

              <tr>

                <th>ID</th>

                <th>User</th>

                <th>Resource</th>

                <th>Access Type</th>

                <th>Status</th>

                <th>Actions</th>

              </tr>

            </thead>

            <tbody>

              {loading ? (

                <tr>

                  <td
                    colSpan="6"
                    className="empty-state"
                  >
                    Loading requests...
                  </td>

                </tr>

              ) : filteredRequests.length === 0 ? (

                <tr>

                  <td
                    colSpan="6"
                    className="empty-state"
                  >

                    {requests.length === 0

                      ? "No requests available."

                      : "No matching requests found."}

                  </td>

                </tr>

              ) : (

                currentRequests.map(
                  (request) => (

                    <tr
                      key={request.id}
                      onClick={() =>
                        setSelectedRequest(
                          request
                        )
                      }
                      style={{
                        cursor:
                          "pointer",
                      }}
                    >

                      <td>
                        {request.id}
                      </td>

                      <td>
                        {request.userName}
                      </td>

                      <td>
                        {request.resourceName}
                      </td>

                      <td>
                        {request.accessType}
                      </td>

                      <td>

                        <StatusBadge
                          status={
                            request.status
                          }
                        />

                      </td>

                      <td>

                        <div className="action-buttons">

                          <button
                            className="action-button approve"
                            type="button"
                            onClick={(e) => {

                              e.stopPropagation();

                              approveRequest(
                                request.id
                              );
                            }}
                          >
                            Approve
                          </button>

                          <button
                            className="action-button reject"
                            type="button"
                            onClick={(e) => {

                              e.stopPropagation();

                              rejectRequest(
                                request.id
                              );
                            }}
                          >
                            Reject
                          </button>

                        </div>

                      </td>

                    </tr>
                  )
                )
              )}

            </tbody>

          </table>

          {/* ✅ PAGINATION */}
          {filteredRequests.length >
            itemsPerPage && (

            <div className="pagination-controls">

              {[...Array(totalPages)]
                .map((_, index) => (

                  <button
                    key={index}
                    className={`pagination-button ${
                      currentPage ===
                      index + 1
                        ? "active"
                        : ""
                    }`}
                    onClick={() =>
                      setCurrentPage(
                        index + 1
                      )
                    }
                    type="button"
                  >
                    {index + 1}
                  </button>
                ))}

            </div>
          )}

        </div>

        {/* ✅ MODAL */}
        <RequestModal
          request={selectedRequest}
          onClose={() =>
            setSelectedRequest(null)
          }
        />

      </div>

    </>
  );
}

export default AdminDashboard;