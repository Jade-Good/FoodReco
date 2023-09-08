from surprise import KNNBasic, SVD, SVDpp,NMF
from surprise import Dataset
from surprise.model_selection import cross_validate



data = Dataset.load_builtin('ml-100k', prompt=False)

# KNN을 사용한 협업 필터링
model = KNNBasic()
cross_validate(model, data, measures=['rmse', 'mae'], cv=5, n_jobs=4, verbose=True)

# SVD를 사용한 협업 필터링
model = SVD()
cross_validate(model, data, measures=['rmse', 'mae'], cv=5, n_jobs=4, verbose=True)

# NMF를 사용한 협업 필터링
model = NMF()
cross_validate(model, data, measures=['rmse', 'mae'], cv=5, n_jobs=4, verbose=True)

# SVDplusplus를 사용한 협업 필터링
model = SVDpp()
cross_validate(model, data, measures=['rmse', 'mae'], cv=5, n_jobs=4, verbose=True)