import React, { useState, useEffect } from "react";
import Chart from "react-apexcharts";
import api from "../../utils/axios";

interface DonutProps {}

interface DonutState {
  options: object;
  series?: number[];
  labels?: string[]; // Corrected type to string[]
}

interface RecentFood {
  foodImg: string;
  foodName: string;
  foodseq: number;
}

interface typeRate {
  rating: number;
  type: string;
}

const Donut: React.FC<DonutProps> = () => {
  const [typeRates, setTypeRates] = useState<typeRate[] | null>([]);
  const [customSeries, setCustomSeries] = useState<number[]>([1, 2]);
  const [customLabels, setCustomLabels] = useState<string[]>(["A", "B"]);
  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/member/home`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      })
      .then((res) => {
        setTypeRates(res.data.typeRates);
        console.log(res);
      })
      .catch((err) => {
        console.error("Error occurred:", err);
      });
  }, []);

  useEffect(() => {
    // Check if typeRates is available
    if (typeRates) {
      // Extract unique type values
      const types = Array.from(new Set(typeRates.map((item) => item.type)));

      // Initialize series and labels arrays
      const series: number[] = [];
      const labels: string[] = [];

      // Loop through unique types
      types.forEach((type) => {
        // Filter typeRates by the current type
        const typeData = typeRates.filter((item) => item.type === type);

        // Calculate the sum of ratings for the current type
        const totalRating = typeData.reduce(
          (sum, item) => sum + item.rating,
          0
        );

        // Add the sum to the series array
        series.push(totalRating);

        // Add the type label to the labels array
        labels.push(type);
      });
      console.log(labels);
      console.log(series);
      // Update the state with custom series and labels
      // setState({ series: customSeries, labels: customLabels });
      setCustomSeries(series);
      setCustomLabels(labels);
      setState({
        options: {
          labels: {
            customLabels: labels,
          },
        },
        series: series,
        labels: labels,
      });
    }
  }, [typeRates]);

  const [state, setState] = useState<DonutState>({
    options: { labels: { customLabels } },
    series: customSeries,
    labels: customLabels,
  });

  console.log(customLabels[0]);
  return (
    <div className="donut" style={{ width: "90vw" }}>
      {customSeries && customLabels ? (
        <Chart
          options={state}
          series={customSeries}
          // label={customLabels}
          type="donut"
        />
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default Donut;
