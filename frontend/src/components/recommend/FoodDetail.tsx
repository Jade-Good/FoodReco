import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import styled, { css } from "styled-components";

import Modal from "react-modal";
import { useRecoilState } from "recoil";
import { foodDetailModal } from "../../recoil/atoms/modalState";

import { Map, MapMarker } from "react-kakao-maps-sdk";

import SearchIcon from "@mui/icons-material/Search";

interface Stores {
  address_name?: string;
  category_name?: string;
  phone?: string;
  place_name?: string;
  place_url?: string;
  road_address_name?: string;
  x?: number;
  y?: number;
}

interface FoodDetailProps {
  foodName: string;
  longitude: number;
  latitude: number;
}

const FoodDetail: React.FC<FoodDetailProps> = ({
  foodName,
  longitude,
  latitude,
}) => {
  // -------------------------- 모달 --------------------------
  const [modal, setModalOpen] = useRecoilState(foodDetailModal);

  // -------------------------- 검색 --------------------------

  // const [info, setInfo] = useState<any>(); // info 상태의 타입은 여러 가지 일 수 있으므로 any를 사용합니다.
  const [markers, setMarkers] = useState<any[]>([]); // markers 상태의 타입은 여러 가지 객체 배열일 수 있으므로 any[]를 사용합니다.
  const [map, setMap] = useState<any>(); // map 상태의 타입은 여러 가지 일 수 있으므로 any를 사용합니다.

  const mapLevel = [50, 100, 250, 500, 1000]; // 지도 레벨

  const ps = new kakao.maps.services.Places(); // 위치 기반 검색
  const [stores, setStores] = useState<Stores[]>([]); // 검색 결과

  const [clicked, setClicked] = useState(false); // 재검색 버튼
  const [listclicked, setListClicked] = useState<boolean[]>([]); // 리스트 클릭 버튼

  // ---------------------------------------------------------

  // 최초 1회 검색 및 지도 생성
  useEffect(() => {
    if (!map) return;

    setMarkers([]);
    setStores([]);

    map.setMinLevel(3); // 맵 최소레벨
    map.setMaxLevel(7); // 맵 최대레발

    ps.keywordSearch(foodName, placesSearchCB, {
      category_group_code: "FD6",
      x: Number(longitude),
      y: Number(latitude),
      radius: 2000,
      sort: kakao.maps.services.SortBy.DISTANCE,
    });
  }, [map]);

  // 장소검색이 완료됐을 때 호출되는 콜백함수
  function placesSearchCB(data: any, status: any, pagination: any) {
    if (!map) return;
    if (status === kakao.maps.services.Status.OK) {
      // 검색 결과 저장
      setStores(data);
      setListClicked(new Array(data.length).fill(false));

      // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
      // LatLngBounds 객체에 좌표를 추가합니다
      // const bounds = new kakao.maps.LatLngBounds();
      const newMarkers: any[] = [];

      for (let i = 0; i < data.length; i++) {
        newMarkers.push({
          position: {
            lat: data[i].y,
            lng: data[i].x,
          },
          content: data[i].place_name,
        });
        // bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
      }
      setMarkers(newMarkers);

      // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
      // map.setBounds(bounds);
    }
  }

  // 버튼을 눌러서 지도 중심으로 재검색
  const search = () => {
    console.log("음식점 검색");

    // 현재 맵 중심 좌표
    let latlng = map.getCenter();

    // info창 지우기
    // setInfo(null);

    ps.keywordSearch(foodName, placesSearchCB, {
      category_group_code: "FD6",
      x: latlng.getLng(),
      y: latlng.getLat(),
      radius: mapLevel[map.getLevel() - 3] * 2,
      sort: kakao.maps.services.SortBy.DISTANCE,
    });
  };

  // 마커 누르면 리스트 스크롤 이동
  const listRef = useRef<HTMLDivElement | null>(null);

  const scrollToItem = (idx: number) => {
    const listItemHeight = 160; // 각 항목의 높이 (예시)

    if (listRef.current) {
      listRef.current.scrollTop = idx * listItemHeight;
    }
  };

  // 리스트 누르면 지도 위치 이동
  const moveToMap = (idx: number) => {
    // 이동할 위도 경도 위치를 생성합니다
    const moveLatLon = new kakao.maps.LatLng(
      markers[idx].position.lat,
      markers[idx].position.lng
    );

    // 지도 중심을 이동 시킵니다
    map.setCenter(moveLatLon);

    // 현재 맵 영역
    // const bounds = map.getBounds();

    // // 이동하려는 좌표가 현재 맵 영역에 포함되지 않으면 이동
    // if (!bounds.contain(moveLatLon)) {
    //   map.panTo(moveLatLon);
    // } else {
    //   map.setCenter(moveLatLon);
    // }

    // 지도 레벨 변경
    map.setLevel(4);
  };

  return (
    <Modal
      isOpen={modal.modalOpen}
      onRequestClose={() => setModalOpen({ modalOpen: false })}
      style={customModalStyles}
      ariaHideApp={false}
      contentLabel="Pop up Message"
      shouldCloseOnOverlayClick={true}
    >
      <MapWrap>
        <h4 style={{ margin: "1rem 0rem 1rem 1rem" }}>
          주변 '{foodName}' 가게
        </h4>

        <SearchButtonLayout>
          <SearchButtonStyle
            onClick={search}
            clicked={clicked}
            onTouchStart={() => {
              setClicked(true);
            }}
            onTouchEnd={() => {
              setClicked(false);
            }}
          >
            <SearchIcon
              fontSize="small"
              style={{ transform: "translate(-5%, 25%)" }}
            />
            <p style={{ display: "inline-block" }}>현 지도에서 검색</p>
          </SearchButtonStyle>
        </SearchButtonLayout>
        <Map // 로드뷰를 표시할 Container
          center={{
            lat: Number(latitude),
            lng: Number(longitude),
          }}
          style={{
            width: "100%",
            height: "25vh",
          }}
          level={7}
          onCreate={setMap}
        >
          {/* 현위치 마커 */}
          <MapMarker
            position={{
              lat: Number(latitude),
              lng: Number(longitude),
            }}
            image={{
              src: "/images/현위치 마커.png",
              size: {
                width: 50,
                height: 50,
              },
            }}
          />
          {/* 검색 결과 가게들 마커 */}
          {markers.map((marker: any, idx: number) => (
            <MapMarker
              key={`marker-${marker.content}-${marker.position.lat},${marker.position.lng}`}
              position={marker.position}
              onClick={() => {
                scrollToItem(idx);
                moveToMap(idx);
              }}
            >
              {/* {info && info.content === marker.content && (
                <span style={{ color: "#000" }}>{marker.content}</span>
              )} */}
            </MapMarker>
          ))}
        </Map>
      </MapWrap>

      <div
        ref={listRef}
        style={{ height: "46vh", overflowY: "scroll", marginTop: "6vh" }}
      >
        {stores
          ? stores.map((result, idx) => {
              return (
                <StoreList
                  onClick={() => {
                    moveToMap(idx);
                  }}
                  touched={listclicked[idx]}
                  onTouchStart={() => {
                    let copy = [...listclicked];
                    copy[idx] = true;
                    setListClicked(copy);
                  }}
                  onTouchEnd={() => {
                    let copy = [...listclicked];
                    copy[idx] = false;
                    setListClicked(copy);
                  }}
                >
                  <hr style={{ border: "solid 1px #ececec", margin: "0" }} />
                  <div style={{ padding: "1vmin 3vmin 1vmin 3vmin" }}>
                    <h3 style={{ margin: "1rem 0 0.5rem 0" }}>
                      {result.place_name}
                    </h3>
                    {result.address_name}
                    <p style={{ color: "gray", marginBottom: "0.5rem" }}>
                      (지번) {result.road_address_name}
                    </p>
                    {String(result.phone).length > 0 ? (
                      <StoreTel href={`tel:${result.phone}}`}>
                        {result.phone}
                      </StoreTel>
                    ) : (
                      <p
                        style={{
                          display: "inline",
                          fontSize: "1rem",
                          fontWeight: "100",
                        }}
                      >
                        번호 미입력
                      </p>
                    )}
                    {"  "}|{"  "}
                    <StoreLink href={result.place_url}>상세보기</StoreLink>
                    <p
                      style={{
                        color: "#b8b8b8",
                        marginTop: "0.5rem",
                        marginBottom: "1rem",
                      }}
                    >
                      {result.category_name}
                    </p>
                  </div>
                </StoreList>
              );
            })
          : null}
      </div>
    </Modal>
  );
};

export default FoodDetail;

// isOpen
// 모달 창이 표시되어야 하는지 여부를 설명하는 boolean 값이다.
// 즉, 해당 값이 true여야 모달 창이 열리는 것이다.

// onRequestClose
// 모달이 닫힐 때 실행될 함수를 의미한다.
// 즉,사용자가 모달을 닫으려고 할 때 실행되는 함수이다.

// style
// 모달 창과 모달 창 바깥에 대한 style을 지정해준다.

// ariaHideApp
// appElement를 숨길지 여부를 나타내는 boolean 값입니다.
// 이 값이 true이면 appElement가 숨겨준다.

// contentLabel
// 스크린리더 사용자에게 콘텐츠를 전달할 때
// 사용되는 방법을 나타내는 문자열이다.

// shouldCloseOnOverlayClick
//팝업창이 아닌 바깥 부분에서 클릭하였을 때, 닫히도록 할 것인지에 대한 처리이다.
// 기본값으로는 true를 가지고 있다.

// CSS

/*overlay는 모달 창 바깥 부분을 처리하는 부분이고,
content는 모달 창부분이라고 생각하면 쉬울 것이다*/
const customModalStyles: ReactModal.Styles = {
  overlay: {
    backgroundColor: " rgba(0, 0, 0, 0.4)",
    width: "100%",
    height: "100vh",
    zIndex: "10",
    position: "fixed",
    top: "0",
    left: "0",
  },
  content: {
    width: "90vw",
    height: "80vh",
    // zIndex: "150",
    // position: "absolute",
    top: "50%",
    left: "50%",

    padding: "0",

    transform: "translate(-50%, -50%)",
    borderRadius: "1rem",
    boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
    backgroundColor: "white",
    justifyContent: "center",
    overflow: "hidden",
  },
};

const MapWrap = styled.div`
  // layout
  position: sticky;
  top: 0%;
  /* left: 0%; */
  width: 100%;
  height: 25vh;
`;

const SearchButtonLayout = styled.div`
  // layout
  position: absolute;
  top: 33%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
`;

const SearchButtonStyle = styled.button<{ clicked: boolean }>`
  display: inline-block;
  padding: 0.5vmin 1rem 0.5rem 0.5rem;
  /* width: 40vmin; */
  /* height: 7vmin; */
  border-radius: 1rem;
  background-color: ${(props) => (props.clicked ? "whitegray" : "white")};
  border: 0px;
  box-shadow: 0 0 0.5rem rgba(0, 0, 0, 0.4);
  /* font-size: 0.8rem; */
  color: #fe9d3a;
  font-weight: bold;
`;

const StoreList = styled.div<{ touched: boolean }>`
  // layout
  background-color: ${(props) => (props.touched ? "#dbe8ff" : "white")};
  height: 10rem;
`;

const StoreLink = styled.a`
  text-decoration: none;
  font-size: 1rem;
  font-weight: bold;
  color: #fe9d3a;

  &:visited {
    color: #d49c64;
  }
`;

const StoreTel = styled.a`
  text-decoration: none;
  font-size: 1rem;
  color: #43a800;

  &:visited {
    color: #43a800;
  }
`;
