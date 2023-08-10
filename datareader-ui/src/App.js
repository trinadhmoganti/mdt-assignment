import React, { useState, useEffect } from 'react';

const App = () => {
  const [initialMdtData, setinitialMdtData] = useState({});
  const [mdtData, setMdtData] = useState({});
  const [generatorResults, setGeneratorResults] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/read-data')
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setinitialMdtData(data);
      })
      .catch((err) => {
        console.log(err.message);
      });
  }, []);

  useEffect(() => {
    fetch('http://localhost:8080/start-generators')
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setGeneratorResults(data);
      })
      .catch((err) => {
        console.log(err.message);
      });
  }, []);

  const getGeneratorResults = async (mdtData) => {
    const mdtDataJson = JSON.parse(mdtData);
    await fetch('http://localhost:8080/restart-generators', {
      method: 'POST',
      body: JSON.stringify({
        datasets: mdtDataJson.datasets,
        generators: mdtDataJson.generators
      }),
      headers: {
        'Content-type': 'application/json',
     },
    })
      .then((response) => response.json())
      .then((data) => {
        setGeneratorResults(data);
      })
      .catch((err) => {
        console.log(err.message);
      });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    getGeneratorResults(mdtData);
  };

  return (
    <div className="app">
      <div className="read-data-container">
        <form onSubmit={handleSubmit}>
        <p>Read only text area</p>
          <textarea readOnly name="initialMdtData" className="form-control" cols="100" rows="5"
            value={JSON.stringify(initialMdtData)}
            onChange={null}
          ></textarea>
          <p>Copy Paste and Edit the JSON in text area below</p>
          <textarea name="mdtData" className="form-control" cols="100" rows="10"
            onChange={(e) => setMdtData(e.target.value)}
          ></textarea>
          <br /><br />
          <button type="submit">Get Generator Results</button>
        </form>
      </div>

      <div className="results-container">
        {generatorResults.map((genResult) => {
          return (
            <div className="result-card" key={genResult}>
              <p className="result">{genResult}</p>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default App;
