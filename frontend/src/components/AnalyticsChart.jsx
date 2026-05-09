import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";

function AnalyticsChart({
  pendingCount,
  approvedCount,
  rejectedCount,
}) {

  const data = [

    {
      name: "Pending",
      count: pendingCount,
    },

    {
      name: "Approved",
      count: approvedCount,
    },

    {
      name: "Rejected",
      count: rejectedCount,
    },
  ];

  return (

    <div
      style={{
        width: "100%",
        height: "300px",
        marginTop: "30px",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >

      <BarChart width={500} height={300} data={data}>

        <CartesianGrid strokeDasharray="3 3" />

        <XAxis dataKey="name" />

        <YAxis />

        <Tooltip />

        <Bar dataKey="count" fill="#8884d8" />

      </BarChart>

    </div>
  );
}

export default AnalyticsChart;
