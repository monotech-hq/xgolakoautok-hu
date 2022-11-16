
(ns site.xgo.pages.main-page.frontend.sections.types.views
  (:require [re-frame.api :as r]
            [site.xgo.pages.main-page.frontend.sections.types.slider :as slider]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- model-name [name]
  [:div {:id "xgo-type--model-name"}
    name])

(defn- type-name [name]
  [:div {:class "xgo-type--name"}
    name]) 

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
  [:div {:class "xgo-type--back-button"}])

(defn- vehicle-type [[id {:type/keys [name images] :as data}]]
  [:div {:key   id
         :class "xgo-type"}
    [type-name name]
    [type-images images]
    (str data)])

(defn- types [{:keys [types-data model-data] :as view-props}]
  [:div {:id "xgo-types"}
    [model-name model-data]
    (doall (map vehicle-type types-data))])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view []
  (let [view-props {:types-data @(r/subscribe [:types/by-model])
                    :model-data @(r/subscribe [:models.selected/name])}]
    [:section {:id "xgo-types--container"}
      [types view-props]]))
