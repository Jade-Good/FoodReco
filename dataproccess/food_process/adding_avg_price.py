import pandas as pd
import json
from fuzzywuzzy import fuzz
from fuzzywuzzy import process
from concurrent.futures import ProcessPoolExecutor

def find_representative_string(str_list):
    avg_similarities = {}
    for s in str_list:
        similarities = [process.extractOne(s, [x for x in str_list if x != s])[1] for x in str_list if x != s]
        avg_similarities[s] = sum(similarities) / len(similarities)
    return max(avg_similarities, key=avg_similarities.get)
def get_avg_price(food_name, menus):
    max_similarity = 0
    avg_price = None
    for menu in menus:
        menu_name_list = menu.get('menus', '')
        # 리스트 내 대표메뉴 추출
        representative_menu = find_representative_string(menu_name_list)
        menu_avg_price = menu.get('avgPrice', None)
        similarity = fuzz.token_set_ratio(food_name, representative_menu)
        if similarity > max_similarity:
            max_similarity = similarity
            avg_price = menu_avg_price
    return avg_price

def get_avg_price_wrapper(args):
    return get_avg_price(*args)

def parallel_get_avg_price(food_names, menus):
    with ProcessPoolExecutor() as executor:
        results = list(executor.map(get_avg_price_wrapper, [(food_name, menus) for food_name in food_names]))
    return results

def main():
    # Load the Excel file
    excel_filepath = 'clustered_new_ingredient_501_clusters.xlsx'
    excel_data = pd.read_excel(excel_filepath)

    # Load the JSON file
    json_filepath = './preprocessed_data/updated_new_clustered.json'
    with open(json_filepath, 'r', encoding='utf-8') as f:
        json_data = json.load(f)

    menus = json_data

    # Apply the get_avg_price function in parallel to add the avgPrice column to the Excel data.
    excel_data['avgPrice'] = parallel_get_avg_price(excel_data['음식명'], menus)

    # Save the modified Excel data to a new file.
    output_filepath = './preprocessed_data/menu_price/clustered_with_avg_price.xlsx'
    excel_data.to_excel(output_filepath, index=False)



if __name__ == '__main__':
    main()