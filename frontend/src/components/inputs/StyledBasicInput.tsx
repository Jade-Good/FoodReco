import React from 'react';
import styled from 'styled-components';

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
  width: 18.4rem;
  height: 2.625rem;
  color: #c6c5c5;
  border: 1px solid #fe9d3a;
  border-radius: 5px;
  &:focus-within {
    border-color: orange; /* 포커스가 입력란 또는 하위 요소에 있을 때 테두리 색상 변경 */
  }
`;

const StyledInput = styled.input`
  flex-shrink: 0;
  padding-left: 2rem;
  border: none;
  outline: none;
  color: #525252;
  font-weight: bold;
`;

// useController을 사용하는 컴포넌트를 위한 type 지정
export type TControl<T extends FieldValues> = {
  type?: string;
  placeholder?: string;
  styleInput?: React.CSSProperties;
  styleContainer?: React.CSSProperties;
  className?: string;
  control?: Control<T>;
  name: FieldPath<T>;
  rules?: Omit<
    RegisterOptions<T>,
    'valueAsNumber' | 'valueAsDate' | 'setValueAs' | 'disabled'
  >;
  maxLength?: number;
};

const StyledBasicInput: React.FC<TControl<any>> = ({
  className,
  placeholder,
  styleInput,
  name,
  rules,
  control,
  type,
  maxLength,
  styleContainer,
}) => {
  const {
    field: { value, onChange },
  } = useController({ name, rules, control });

  return (
    <InputContainer style={styleContainer}>
      <StyledInput
        maxLength={maxLength}
        className={className}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        style={styleInput}
        type={type}
      />
    </InputContainer>
  );
};

export default StyledBasicInput;
