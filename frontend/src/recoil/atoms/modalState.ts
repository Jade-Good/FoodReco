import { atom } from "recoil";

export const foodDetailModal = atom({
  key: "foodDetailModal",
  default: {
    modalOpen: false,
  },
}); 