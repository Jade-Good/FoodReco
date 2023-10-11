import * as React from 'react';
import { useForm, useFieldArray, useWatch, Control } from 'react-hook-form';

type FormValues = {
  food: {
    name: string;
    // price: number;
    // quantity: number;
  }[];
};

const Total = ({ control }: { control: Control<FormValues> }) => {
  const formValues = useWatch({
    name: 'food',
    control,
  });
  //   const total = formValues.reduce(
  //     (acc, current) => acc + (current.price || 0) * (current.quantity || 0),
  //     0
  //   );
  //   return <p>Total Amount: {total}</p>;
};

export default function StyledFoodSelect() {
  const foodList = ['aaa', 'bbb', 'ccc', 'ddd', 'eee', 'fff', 'ggg'];

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>({
    defaultValues: {
      food: [{ name: 'test' }],
    },
    mode: 'onBlur',
  });
  const { fields, append, remove } = useFieldArray({
    name: 'food',
    control,
  });
  const onSubmit = (data: FormValues) => console.log(data);

  return (
    <div>
      <form onSubmit={handleSubmit(onSubmit)}>
        {foodList.map((field: string, index: number) => {
          return (
            <div key={index}>
              <section className={'section'} key={index}>
                <input
                  placeholder="name"
                  {...register(`food.${index}.name` as const, {
                    required: true,
                  })}
                  value={field}
                  className={errors?.food?.[index]?.name ? 'error' : ''}
                  readOnly
                />

                {/* <input
                  placeholder="quantity"
                  type="number"
                  {...register(`cart.${index}.quantity` as const, {
                    valueAsNumber: true,
                    required: true,
                  })}
                  className={errors?.cart?.[index]?.quantity ? 'error' : ''}
                />
                <input
                  placeholder="value"
                  type="number"
                  {...register(`cart.${index}.price` as const, {
                    valueAsNumber: true,
                    required: true,
                  })}
                  className={errors?.cart?.[index]?.price ? 'error' : ''}
                /> */}
                <button type="button" onClick={() => remove(index)}>
                  DELETE
                </button>
              </section>
            </div>
          );
        })}

        {/* <Total control={control} /> */}

        <button
          type="button"
          onClick={() =>
            append({
              name: '',
              //   quantity: 0,
              //   price: 0,
            })
          }
        >
          APPEND
        </button>
        <input type="submit" />
      </form>
    </div>
  );
}
