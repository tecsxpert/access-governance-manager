export function exportToCSV(
  requests
) {

  const headers = [
    "ID",
    "User",
    "Resource",
    "Access Type",
    "Status",
  ];

  const rows = requests.map(
    (request) => [

      request.id,

      request.userName,

      request.resourceName,

      request.accessType,

      request.status,
    ]
  );

  let csvContent =
    headers.join(",") + "\n";

  rows.forEach((row) => {

    csvContent +=
      row.join(",") + "\n";
  });

  const blob = new Blob(
    [csvContent],
    {
      type: "text/csv",
    }
  );

  const url =
    window.URL.createObjectURL(blob);

  const link =
    document.createElement("a");

  link.href = url;

  link.download =
    "access_requests.csv";

  link.click();
}
