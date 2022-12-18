(ns site.components.frontend.table.views
  (:require [elements.api :as elements]
            [re-frame.api :as r]))

(defn th [name]
  [:th {:key name} name])

(defn thead [data]
  [:thead
   [:tr
    (map th data)]])

(defn td [param]
  [:td {:key param} param])

(defn create-tr [tr-model]
  (cond
    (var? tr-model)    tr-model
    (vector? tr-model) (fn [b] (conj tr-model b))
    :else (fn [a]
            [:tr (map (fn [b]
                        [td (get a b)])
                      tr-model)])))

(defn tbody [{:keys [tr-model data]}]
  (let [tr (create-tr tr-model)]
    [:tbody
     (map (fn [row-data]
            ^{:key (random-uuid)}
            [tr row-data])
          data)]))

(defn table [config]
  [:div.table-container
   [:table.table
    [thead (:thead config)]
    [tbody config]]])

(defn component [config]
  [:div
    [table config]])
