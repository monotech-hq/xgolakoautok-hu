
(ns site.xgo.pages.main-page.frontend.sections.types.views
  (:require [re-frame.api :as r]
            [site.xgo.pages.main-page.frontend.sections.types.slider :as slider]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- model-name [name]
  [:div {:id "xgo-type--model-name"}
    name])

(defn- type-name [[id {:type/keys [name]}]]
  [:div {:key id :class "xgo-type--name"}
    (str name)]) 

(defn- type-name-button [[id {:type/keys [name]}]]
  [:button {:key id :class "xgo-type--type-name-button"
            :on-click #(r/dispatch [:type/select! id])}
   [:div.xgo-type--name name]])

(defn- header [{:keys [types-data model-data]}]
  [:<>
    [model-name model-data]
    (if (= 1 (count types-data))
      [type-name (first (seq types-data))]
      (map type-name-button types-data))])

(defn- type-images [images]
  [:div {:class "xgo-type--images"}
    [slider/view 
      (map (fn [{:media/keys [id uri]}]
              [:img {:src uri}])
           images)]])

(defn- type-table []
  [:div {:class "xgo-type--table"}])

(defn- type-files []
  [:div {:class "xgo-type--files"}])

(defn- type-back-button []
  [:button {:class "xgo-type--back-button"
            :on-click #(let [element (.getElementById js/document "xgo-categories")]
                         (.scrollIntoView element
                           (clj->js {:behavior "smooth"
                                     :block    "start"
                                     :inline   "center"})))}
        "Vissza"])    

(defn- vehicle-type [[id {:type/keys [name images] :as data}]]
  [:div {:key   id
         :class "xgo-type"}
    [type-images images]
    (str data)
    [type-back-button]])

(defn- types [{:keys [types-data] :as view-props}]
  [:div {:id "xgo-types"}
    [header view-props]
    (doall (map vehicle-type types-data))])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view []
  (let [view-props {:types-data @(r/subscribe [:types/by-model])
                    :model-data @(r/subscribe [:models.selected/name])}]
    [:section {:id "xgo-types--container"}
      [types view-props]]))
