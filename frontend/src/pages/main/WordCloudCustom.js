import React, { useState, useEffect, useCallback } from "react";
import Chart from "react-apexcharts";
import WordCloud from "react-d3-cloud";
import { scaleOrdinal } from "d3-scale";
import { schemeCategory10 } from "d3-scale-chromatic";
import api from "../../utils/axios";

const WordCloudCustom = () => {
  const [recentFoods, setRecentFoods] = useState([]);
  const [foodNameList, setFoodNameList] = useState([]);

  useEffect(() => {
    api
      .get(`${process.env.REACT_APP_BASE_URL}/member/home`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      })
      .then((res) => {
        setRecentFoods(res.data.recentFoods);
        console.log(res);
      })
      .catch((err) => {
        console.error("Error occurred:", err);
      });
  }, []);
  useEffect(() => {
    if (recentFoods) {
      const names = recentFoods.map((food) => food.foodName);
      setFoodNameList(names);
    }
  }, [recentFoods]);

  const data = foodNameList.map(function (d) {
    return { text: d, value: 10 + Math.random() * 90 };
  });

  const fontSize = useCallback((word) => Math.log2(word.value) * 5, []);
  const rotate = useCallback((word) => word.value % 360, []);
  const fill = useCallback((d, i) => scaleOrdinal(schemeCategory10)(i), []);
  const onWordClick = useCallback((word) => {
    console.log(`onWordClick: ${word}`);
  }, []);
  const onWordMouseOver = useCallback((word) => {
    console.log(`onWordMouseOver: ${word}`);
  }, []);
  const onWordMouseOut = useCallback((word) => {
    console.log(`onWordMouseOut: ${word}`);
  }, []);

  return (
    <div style={{ width: "90vw" }}>
      <WordCloud
        data={data}
        width={500}
        height={500}
        font="Times"
        fontStyle="italic"
        fontWeight="bold"
        fontSize={fontSize}
        spiral="rectangular"
        rotate={rotate}
        padding={5}
        random={Math.random}
        fill={fill}
        onWordClick={onWordClick}
        onWordMouseOver={onWordMouseOver}
        onWordMouseOut={onWordMouseOut}
      />
    </div>
  );
};

export default WordCloudCustom;
