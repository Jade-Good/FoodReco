import React from 'react';
import styled from 'styled-components';
import { AiOutlineLock } from 'react-icons/ai';

const InputContainer = styled.div`
  display: flex;
  align-items: center;
  border: 3px solid #c6c5c5;
  width: 17.125rem;
  height: 4.6875rem;
`;

const StyledInput = styled.input`
  fill: #fff;
  stroke-width: 3px;
  stroke: #c6c5c5;
  flex-shrink: 0;
  padding-left: 2rem;
  border: none; /* 입력란 테두리 제거 */
  outline: none; /* 입력란 포커스 시 외곽선 제거 */
`;

const Icon = styled(AiOutlineLock)`
  font-size: 1.5rem;
  flex-shrink: 0;
  color: #c6c5c5;
`;

interface StyledBasicInputWithIconProps {
  placeholder: string;
  style?: React.CSSProperties;
}

const StyledPwInput: React.FC<StyledBasicInputWithIconProps> = ({
  placeholder,
  style,
}) => {
  return (
    <InputContainer style={style}>
      <Icon />
      <StyledInput placeholder={placeholder} />
    </InputContainer>
  );
};

export default StyledPwInput;
