
(ns site.xgo.pages.main-page.frontend.sections.models.views
  (:require [re-frame.api :as r]
            [elements.api :as x.elements]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn dimension [icon num]
  [:div {:class "xgo-model-card--dimension"}
    [x.elements/icon {:icon  icon
                      :class :xgo-model-card--dimension-icon}]
    [:div {:class "xgo-model-card--label"} num]])

(defn model-dimensions []
 [:div {:class "xgo-model-card--dimensions"}
   [dimension "airline_seat_recline_normal" 4]
   [dimension "airline_seat_flat" 2]]) 

(defn model-name [name]
  [:p {:class "xgo-model-card--name"}
    name])

(defn model-thumbnail [{:media/keys [uri]}]
   [:img {:class "xgo-model-card--thumbnail"
          :src   uri}])

(defn- model [[id {:keys [name thumbnail] :as model-data}]]
 [:button {:key      id
           :on-click #(r/dispatch [:models/select! name])}
   [:div {:class "xgo-model-card"}
     [model-dimensions model-data]
     [model-name       name]
     [model-thumbnail  thumbnail]]])
  
(defn- models [{:keys [models-data]}]
  [:div {:id "xgo-models"}
    (if (empty? models-data)
      [:p "Empty..."]
      (doall (map model models-data)))])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn view []
  (let [view-props {:models-data @(r/subscribe [:models/by-category])}]
    [:section {:id "xgo-models--container"}
              [models view-props]]))
