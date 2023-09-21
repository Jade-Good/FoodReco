import React, { useState } from 'react';
import styled from 'styled-components';
import { BiLockAlt } from 'react-icons/bi';
import {
  Control,
  FieldPath,
  FieldValues,
  RegisterOptions,
  useController,
} from 'react-hook-form';
import { MdOutlineVisibilityOff } from 'react-icons/md';
import { MdOutlineVisibility } from 'react-icons/md';
const InputContainer = styled.div`
  display: flex;
  align-items: center;
  width: 18.2rem;
  height: 4rem;
  background-color: #fff6ec;
`;

const StyledInput = styled.input`
  background-color: #fff6ec;
  stroke-width: 3px;
  stroke: #c6c5c5;
  flex-shrink: 0;
  color: #7d7b7b;

  padding-left: 2rem;
  border: none; /* 입력란 테두리 제거 */
  outline: none; /* 입력란 포커스 시 외곽선 제거 */
  font-weight: bold;
`;

const Icon = styled(BiLockAlt)`
  font-size: 1.5rem;
  flex-shrink: 0;
  color: #7d7b7b;
  margin-left: 10px;
  height: 7rem;
  weight: 7rem;
`;

const Icon2 = styled(MdOutlineVisibilityOff)`
  font-size: 1.5rem;
  flex-shrink: 0;
  color: #7d7b7b;
  margin-left: 10px;
  height: 7rem;
  weight: 7rem;
`;

const Icon3 = styled(MdOutlineVisibility)`
  font-size: 1.5rem;
  flex-shrink: 0;
  color: #7d7b7b;
  margin-left: 10px;
  height: 7rem;
  weight: 7rem;
`;

export type TControl<T extends FieldValues> = {
  placeholder?: string;
  style?: React.CSSProperties;
  className?: string;
  control: Control<T>;
  name: FieldPath<T>;
  rules?: Omit<
    RegisterOptions<T>,
    'valueAsNumber' | 'valueAsDate' | 'setValueAs' | 'disabled'
  >;
};

const StyledPwInputIcon: React.FC<TControl<any>> = ({
  className,
  placeholder,
  style,
  name,
  rules,
  control,
}) => {
  const {
    field: { value, onChange },
  } = useController({ name, rules, control });
  const [visiblity, setVisiblity] = useState(0);

  return (
    <InputContainer style={style}>
      <Icon />
      <StyledInput
        className={className}
        type={visiblity ? 'text' : 'password'} // 비밀번호 표시 여부에 따라 타입 변경
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        style={style}
      />
      {visiblity ? (
        <Icon3 onClick={() => setVisiblity(0)} />
      ) : (
        <Icon2 onClick={() => setVisiblity(1)} />
      )}
    </InputContainer>
  );
};

export default StyledPwInputIcon;
