import React from "react";
import styled, { css } from "styled-components";

// interface ButtonProps {
interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  color?: string;
  background?: string;
  white?: boolean;
  // onClick?: () => void;
  onClick?: (props: any) => void;
  radius?: string;
  green?: boolean;
  red?: boolean;
  fontSize?: string;
  border?: string;
  width?: string;
  height?: string;
  marginTop?: string;
  marginBottom?: string;
  display?: boolean;
  boxShadow?: string;
}

const BasicButton = styled.button<ButtonProps>`
  padding: 6px 12px;
  line-height: 1.5;
  font-weight: bold;
  justify-content: center;
  font-family: Inter;
  display: true;
  border-radius: ${(props) => props.radius || "4px"};
  color: ${(props) => props.color || "white"};
  background: ${(props) => props.background || "#FE9D3A"};
  font-size: ${(props) => props.fontSize || "24px"};
  border: ${(props) => props.border || "none"};
  width: ${(props) => props.width || "none"};
  height: ${(props) => props.height || "none"};
  margin-top: ${(props) => props.marginTop || "none"};
  margin-bottom: ${(props) => props.marginBottom || "none"};
  box-shadow: ${(props) => props.boxShadow || "none"}

  cursor: pointer;

  ${(props) =>
    props.white &&
    css`
      color: #fe9d3a;
      background: white;
      font-weight: bold;
      border-radius: 10px;
      border: 1px solid #fe9d3a;
    `}
  ${(props) =>
    props.green &&
    css`
      color: white;
      background: #15e506;
      font-weight: bold;
      border-radius: 10px;
    `}
  ${(props) =>
    props.red &&
    css`
      color: white;
      background: #ef5c5c;
      font-weight: bold;
      border-radius: 10px;
    `}
`;

function StyledButtonProps({ children, ...props }: ButtonProps) {
  return (
    <BasicButton {...props} onClick={props.onClick}>
      {children}
    </BasicButton>
  );
}

export default StyledButtonProps;
