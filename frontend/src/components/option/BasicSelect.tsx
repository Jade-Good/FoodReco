import * as React from 'react';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import './BasicSelect.css';
import {
  Control,
  FieldPath,
  FieldValues,
  RegisterOptions,
  useController,
} from 'react-hook-form';

interface OptionsProps {
  options: string[] | number[];
  label: string;
}

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
  options: string[] | number[];
  label: string;
};

const BasicSelect: React.FC<TControl<any>> = ({
  className,
  placeholder,
  style,
  name,
  rules,
  control,
  options,
  label,
}) => {
  const {
    field: { value, onChange },
  } = useController({ name, rules, control });

  return (
    <Box sx={{ minWidth: 120 }}>
      <FormControl fullWidth>
        <InputLabel id="demo-simple-select-label"></InputLabel>
        <Select
          sx={{ borderColor: 'orange !important' }} // 여기에서 borderColor를 주황색(orange)으로 설정
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={value}
          label={label}
          onChange={onChange}
        >
          {options.map((option, index) => (
            <MenuItem key={index} value={option}>
              {option}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Box>
  );
};

export default BasicSelect;
