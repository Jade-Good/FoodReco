import React from 'react';
import styled from 'styled-components';
import { AiOutlineUser } from 'react-icons/ai';
import {
  Control,
  FieldPath,
  FieldValues,
  RegisterOptions,
  useController,
} from 'react-hook-form';

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
  padding-left: 2rem;
  border: none; /* 입력란 테두리 제거 */
  outline: none; /* 입력란 포커스 시 외곽선 제거 */
  color: #7d7b7b;
  font-weight: bold;
`;

const Icon = styled(AiOutlineUser)`
  font-size: 1.5rem;
  flex-shrink: 0;
  color: #7d7b7b;
  margin-left: 10px;
  height: 7rem;
  weight: 7rem;
`;

// useController을 사용하는 컴포넌트를 위한 type 지정
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

const StyledIdInputIcon: React.FC<TControl<any>> = ({
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

  return (
    <InputContainer>
      <Icon />
      <StyledInput
        className={className}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        style={style}
      />
    </InputContainer>
  );
};

export default StyledIdInputIcon;
