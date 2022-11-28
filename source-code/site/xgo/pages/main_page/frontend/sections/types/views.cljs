
(ns site.xgo.pages.main-page.frontend.sections.types.views
  (:require [re-frame.api                 :as r]
            [plugins.reagent.api                  :as reagent]
            [site.components.frontend.api :as site.components]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- model-name [name]
  [:div {:id "xgo-type--model-name"}
    name])

(defn- type-name [[id {:keys [name]}]]
  [:div {:key   id 
         :class "xgo-type--name"}
    (str name)]) 

(defn- type-name-button [[id {:keys [name]}]]
  [:button {:key           id 
            :class         "xgo-type--name-button xgo-type--name"
            :data-selected @(r/subscribe [:types/selected? name])
            :on-click      #(r/dispatch [:types/select! name])}
    name])

(defn- type-name-button-group [types-data]
  [:div {:id "xgo-type--name-button-group"}
      (doall (map type-name-button types-data))])

(defn- header [{:keys [types-data model-data]}]
  [:div {:id "xgo-type--header"}
    [model-name model-data]
    (if (= 1 (count types-data))
      [type-name (first (seq types-data))]
      [type-name-button-group types-data])])

(defn- type-images [images]
  [:div {:id "xgo-type--images"}
    [site.components/slider 
       (map (fn [a] [:div {:key a :style {:height "500px" :display "flex" :align-items "center" :justify-content "center"}} 
                     [:h2 a]
                     "imagine an image here"]) 
           (range 10))]]) 
      ;; (map (fn [{:media/keys [id uri]}]
      ;;         [:img {:src uri}])
      ;;      images)]])

(defn- type-table [id]
  [:div {:class "mt-scheme-table--container" :style {:min-height "550px"}}
    [:h2.mt-scheme-table--header "Műszaki adatok"]
    [site.components/scheme-table {:placeholder :no-visible-data
                                   :scheme-id   :vehicle-types.technical-data
                                   :value-path  [:site :types id]}]])

(defn- type-files []
  [:div {:class "xgo-type--files"}])

(defn- type-back-button []
  [:div {:id "xgo-type--back-button-container"}
   [:button {:id       "xgo-type--back-button"
             :on-click #(r/dispatch [:types.view/back!])}
        "Vissza"]])    

(defn- get-price-quote-button [selected-type]
  [:div {:id "xgo-type--price-quote-button"}
    [:button {:on-click #(r/dispatch [:type.view/get-price-qoute! (:id selected-type)])}
      "Árajánlat kérése"]])

(defn- vehicle-type [{:keys [name] :as data}]
  (reagent/lifecycles 
   {:component-did-mount (fn [] (r/dispatch [:types/select! name]))
    :reagent-render 
    (fn [{:keys [id images] :as data}]
      [:div {:key id
             :id  "xgo-type"}
        [:div {:id "xgo-type--layout"} 
          [type-images images]
          [type-table id]]
        [type-back-button]])}))

(defn- types [{:keys [types-data selected-type] :as view-props}]
  [:div {:id "xgo-type--container"}
    [header view-props]
    [get-price-quote-button selected-type]
    [vehicle-type selected-type]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view []
  (let [view-props {:types-data    @(r/subscribe [:types/by-model])
                    :selected-type @(r/subscribe [:types/selected])
                    :model-data    @(r/subscribe [:models.selected/name])}]
    [:section {:id "xgo-types--container"}
            [types view-props]]))
