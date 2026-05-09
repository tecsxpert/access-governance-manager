import { useEffect, useMemo, useState } from "react";
import { toast } from "react-toastify";
import api, { getAuthHeaders } from "../api";
import Navbar from "../components/NavBar";

function AdminDashboard() {

  const [requests, setRequests] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(1);
  const [isLoading, setIsLoading] = useState(false);
  const itemsPerPage = 10;

  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  // ✅ Fetch Requests
  const fetchRequests = async () => {
    setIsLoading(true);

    try {
      const response = await api.get("/access/all", {
        headers: getAuthHeaders(),
      });

      setRequests(response.data.data || []);
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchRequests();
  }, []);

  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm, statusFilter]);

  const filteredRequests = useMemo(() => {
    const normalizedSearch = searchTerm.trim().toLowerCase();

    return requests.filter((request) => {
      const matchesStatus =
        statusFilter === "ALL" || request.status === statusFilter;

      const matchesSearch =
        !normalizedSearch ||
        [request.id, request.userName, request.resourceName, request.accessType, request.status]
          .some((value) =>
            String(value || "").toLowerCase().includes(normalizedSearch)
          );

      return matchesStatus && matchesSearch;
    });
  }, [requests, searchTerm, statusFilter]);

  const totalCount = requests.length;
  const pendingCount = requests.filter((request) => request.status === "PENDING").length;
  const approvedCount = requests.filter((request) => request.status === "APPROVED").length;
  const rejectedCount = requests.filter((request) => request.status === "REJECTED").length;

  const lastIndex = currentPage * itemsPerPage;
  const firstIndex = lastIndex - itemsPerPage;
  const currentRequests = filteredRequests.slice(firstIndex, lastIndex);
  const totalPages = Math.max(1, Math.ceil(filteredRequests.length / itemsPerPage));

  // ✅ Approve
  const approveRequest = async (id) => {

    try {
      await api.put(`/access/approve/${id}`, {}, {
        headers: getAuthHeaders(),
      });

      toast.success("Request Approved");
      fetchRequests();
    } catch (error) {
      console.log(error);
      toast.error("Approval failed");
    }
  };

  // ✅ Reject
  const rejectRequest = async (id) => {

    try {
      await api.put(`/access/reject/${id}`, {}, {
        headers: getAuthHeaders(),
      });

      toast.error("Request Rejected");
      fetchRequests();
    } catch (error) {
      console.log(error);
      toast.error("Rejection failed");
    }
  };

  return (
    <div>
      <Navbar />
      <div className="dashboard-layout">
        <div className="dashboard-hero">
          <div>
            <h1>Admin Dashboard</h1>
            <p>Manage pending requests, approve access, and keep governance flowing smoothly.</p>
          </div>
          <div className="info-card">
            <h3>Current role</h3>
            <p>{role}</p>
          </div>
        </div>

        <div className="dashboard-cards">
          <div className="info-card">
            <h3>Total requests</h3>
            <p>{totalCount}</p>
          </div>
          <div className="info-card">
            <h3>Pending</h3>
            <p>{pendingCount}</p>
          </div>
          <div className="info-card">
            <h3>Approved</h3>
            <p>{approvedCount}</p>
          </div>
          <div className="info-card">
            <h3>Rejected</h3>
            <p>{rejectedCount}</p>
          </div>
        </div>

        <div className="dashboard-controls">
          <input
            type="search"
            placeholder="Search by ID, user, resource, or status"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="form-input"
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="form-input"
          >
            <option value="ALL">All Statuses</option>
            <option value="PENDING">PENDING</option>
            <option value="APPROVED">APPROVED</option>
            <option value="REJECTED">REJECTED</option>
          </select>
        </div>

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
              {isLoading ? (
                <tr>
                  <td colSpan="6" className="empty-state">
                    Loading requests...
                  </td>
                </tr>
              ) : filteredRequests.length === 0 ? (
                <tr>
                  <td colSpan="6" className="empty-state">
                    {requests.length === 0
                      ? "No requests available yet."
                      : "No requests match your search or filter."}
                  </td>
                </tr>
              ) : (
                currentRequests.map((request) => (
                  <tr key={request.id}>
                    <td>{request.id}</td>
                    <td>{request.userName}</td>
                    <td>{request.resourceName}</td>
                    <td>{request.accessType}</td>
                    <td>
                      <span className={`status-pill status-${request.status?.toLowerCase()}`}>
                        {request.status}
                      </span>
                    </td>
                    <td>
                      <div className="action-buttons">
                        <button
                          className="action-button approve"
                          onClick={() => approveRequest(request.id)}
                          type="button"
                        >
                          Approve
                        </button>
                        <button
                          className="action-button reject"
                          onClick={() => rejectRequest(request.id)}
                          type="button"
                        >
                          Reject
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
          {filteredRequests.length > itemsPerPage && (
            <div className="pagination-controls">
              {[...Array(totalPages)].map((_, index) => (
                <button
                  key={index}
                  className={`pagination-button ${currentPage === index + 1 ? "active" : ""}`}
                  onClick={() => setCurrentPage(index + 1)}
                  type="button"
                >
                  {index + 1}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;