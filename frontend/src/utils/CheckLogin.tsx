// import React from 'react';
// import { Navigate } from 'react-router-dom';
// import { useRecoilState } from 'recoil';
// import { userState } from '../recoil/atoms/userState';

// interface PrivateRouteProps {
//   authenticated: number;
//   component: JSX.Element;
// }

// export const CheckLogin = ({
//   authenticated,
//   component: Component,
// }: PrivateRouteProps) => {
//   const [user, setUser] = useRecoilState(userState);
//   if (user.accessToken) {
//     window.alert('로그인이 필요한 서비스입니다.');
//     return <Navigate to="/" />;
//   }

//   return Component;
// };
import React from 'react';

export const CheckLogin = () => {
  return <div>CheckLogin</div>;
};
