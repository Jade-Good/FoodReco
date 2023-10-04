import { atom } from "recoil";
import { CrewProps } from "../../pages/crew/CrewList";

export const crewDetail = atom<CrewProps>({
  key: "crewDetail",
  default: {
    crewSeq: 0,
    name: "",
    img: "",
    status: "",
    recentRecommend: 0,
    crewCnt: 0,
  },
});
