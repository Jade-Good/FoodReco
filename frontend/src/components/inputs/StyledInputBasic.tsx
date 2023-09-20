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
  width: 17.125rem;
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

const StyledInputBasic: React.FC<TControl<any>> = ({
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

export default StyledInputBasic;
