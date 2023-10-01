import pandas as pd

file_path = './preprocessed_data/menu_ingredient/reordered_deleted_data_0926_1005.xlsx'

ordered_data = pd.read_excel(file_path)


df_cleaned = ordered_data.dropna(how='all')
df_cleaned.to_excel('./preprocessed_data/menu_ingredient/reordered_deleted_data_0926_1005.xlsx', index=False)