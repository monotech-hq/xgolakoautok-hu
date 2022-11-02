
(ns app.storage.backend.media-browser.prototypes
    (:require [app.common.backend.api           :as common]
              [app.storage.backend.core.helpers :as core.helpers]
              [mid-fruits.vector                :as vector]
              [mongo-db.api                     :as mongo-db]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicated-directory-prototype
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props} document]
  (letfn [(f2 [{:media/keys [id] :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                        (vector/->items % f2))]
         (as-> document % (common/duplicated-document-prototype request %)
                          (if (= destination-id parent-id) % (update % :media/path f1)))))

(defn duplicated-file-prototype
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props} document]
  (letfn [(f3 [{:media/keys [id filename] :as %}] (assoc % :media/filename (core.helpers/file-id->filename id filename)))
          (f2 [{:media/keys [id]          :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                                 (vector/->items % f2))]
         (as-> document % (common/duplicated-document-prototype request %)
                          (if (= destination-id parent-id) % (update % :media/path f1))
                          (f3 %))))
