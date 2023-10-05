import { atom } from "recoil";

export const foodDetailModal = atom({
  key: "foodDetailModal",
  default: {
    modalOpen: false,
  },
});

export const crewVoteModal = atom({
  key: "crewVoteModal",
  default: {
    modalOpen: false,
  },
});

export const crewVoteHistorylModal = atom({
  key: "crewVoteHistorylModal",
  default: {
    modalOpen: false,
  },
});
