import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist({
  key: "loginUser",
  storage: sessionStorage,
});

type User = {
  refreshToken: string;
  email: string;
  nickname: string;
  accessToken: string;
  memberSeq: number;
  expAccessToken: number;
};

export const userState = atom<User>({
  key: "userState",
  default: {
    refreshToken: "",
    nickname: "",
    email: "",
    accessToken: "",
    memberSeq: 0,
    expAccessToken: 0,
  },
  effects_UNSTABLE: [persistAtom],
});

export const foodDetailModal = atom({
  key: "foodDetailModal",
  default: {
    modalOpen: false,
  },
  effects_UNSTABLE: [persistAtom],
});
