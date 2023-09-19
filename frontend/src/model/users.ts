export type User = {
  id: number;
  email: string;
  password?: string;
  nickname: string;
  name?: string;
  phone?: number;
  point?: number;
  profileUrl?: string;
  state?: number;
  type?: number;
};

export type Client = {
  user: User;
  clientAttendance?: number;
};

export type Counselor = {
  user: User;
  regist: string;
  license: string;
  school?: string;
  company?: string;
  rate?: number;
};

export type Evaluation = {
  title: string;
  content: string;
  date?: string;
  id: number;
  state?: number;
  rate: number;
};
