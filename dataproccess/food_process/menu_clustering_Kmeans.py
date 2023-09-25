from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans
import numpy as np
import json
import pandas as pd

json_path = "./final_int_price_clustered_utf8.json"
with open(json_path, 'r', encoding='utf-8') as f:
    js = json.load(f)
filtered_df = pd.DataFrame(js)


# Flatten the list of menus into a single list
all_menus = [menu for menus in filtered_df['menus'] for menu in menus]

# Initialize the TF-IDF vectorizer
vectorizer = TfidfVectorizer(analyzer='char', ngram_range=(2, 3))

# Fit and transform the list of menus
X = vectorizer.fit_transform(all_menus)

# Number of clusters (arbitrarily chosen for demonstration)
n_clusters = 10

# Perform K-means clustering
kmeans = KMeans(n_clusters=n_clusters, random_state=50 )
kmeans.fit(X)

# Get cluster labels
labels = kmeans.labels_

# Group menus by their cluster label
clustered_menus = {i: [] for i in range(n_clusters)}
for i, label in enumerate(labels):
    clustered_menus[label].append(all_menus[i])

# Show a sample of menus from each cluster
cluster_samples = {k: v[:10] for k, v in clustered_menus.items()}

data_for_json = filtered_df.to_dict(orient='records')

# JSON 파일로 저장 (한글 인코딩은 UTF-8)
with open('new_clustered.json', 'w', encoding='utf-8') as json_file:
    json.dump(data_for_json, json_file, ensure_ascii=False, indent=2)
