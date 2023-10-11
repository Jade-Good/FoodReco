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
  width: 7.4rem;
  height: 3rem;
  color: #c6c5c5;
  border: 1px solid #fe9d3a;
  border-radius: 5px;
  justify-content: space-between; /* 수평으로 양 옆에 정렬 */
`;

const StyledInput = styled.input`
  flex-shrink: 0;
  padding-left: 2rem;
  border: none;
  outline: none;
  color: #525252;
  // font-weight: bold;
  width: 3rem;
  height: 1.23956rem;
  font-size: 1rem;
`;
const UnitContainer = styled.span`
  flex-shrink: 0;
  padding-right: 0.5rem; /* CM와의 간격을 조절할 수 있음 */
  color: #525252;
`;

// useController을 사용하는 컴포넌트를 위한 type 지정
export type TControl<T extends FieldValues> = {
  type?: string;
  placeholder?: string;
  styleInput?: React.CSSProperties;
  className?: string;
  control?: Control<T>;
  name: FieldPath<T>;
  rules?: Omit<
    RegisterOptions<T>,
    'valueAsNumber' | 'valueAsDate' | 'setValueAs' | 'disabled'
  >;
  unit?: string | number;
  styleContainer?: React.CSSProperties;
};

const StyledBasicInputUnit: React.FC<TControl<any>> = ({
  className,
  placeholder,
  name,
  rules,
  control,
  type,
  unit,
  styleContainer,
  styleInput,
}) => {
  const {
    field: { value, onChange },
  } = useController({ name, rules, control });

  return (
    <InputContainer style={styleContainer}>
      <StyledInput
        className={className}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        style={styleInput}
        type={type}
      />
      {unit && <UnitContainer>{unit}</UnitContainer>}
    </InputContainer>
  );
};

export default StyledBasicInputUnit;
