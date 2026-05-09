function AuditTimeline() {

  const logs = [

    "User1 requested Server READ access",

    "Admin approved Database WRITE access",

    "User2 request rejected",

    "CSV exported by Admin",
  ];

  return (

    <div className="table-card">

      <h2>
        Activity Timeline
      </h2>

      <ul>

        {logs.map((log, index) => (

          <li
            key={index}
            style={{
              marginBottom: "10px",
            }}
          >
            {log}
          </li>
        ))}

      </ul>

    </div>
  );
}

export default AuditTimeline;