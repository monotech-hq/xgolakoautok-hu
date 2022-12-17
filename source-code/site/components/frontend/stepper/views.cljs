
(ns site.components.frontend.stepper.views 
  (:require [re-frame.api     :as r]
            [plugins.reagent.api      :as reagent]
            [elements.api     :as x.elements]
            [x.components.api :as x.components]))
 
;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------
;; ----- Header -----

(defn- step-label [index current-step [step-id {:keys [valid?]}]]
  (let [selected? (= index current-step)]
    [:button {:key           step-id
              :class         "mt-stepper--step-label"
              :data-done     valid?
              :data-selected selected?
              :disabled      (not valid?)
              :on-click      #(r/dispatch [:stepper/select! index])}
       [:span {:style {:font-size "24px" :height "24px" :width "24px"}} index]
       [:span (if (string? step-id)
                (str step-id)
                (x.components/content step-id))]]))

(defn- step-labels [current-step steps]
  [:<> (map-indexed 
          (fn [index step-data]
              [:<> {:key index} 
                [step-label index current-step step-data]
                [x.elements/horizontal-line {:class :mt-stepper--line}]]) 
          (butlast steps))])

(defn- last-step-label [current-step steps]
  (let [all-valid?                    (every? (fn [[_ {:keys [valid?]}]] (true? valid?)) (butlast steps))
        [last-step-id last-step-data] (last steps)
        last-step                     [last-step-id (assoc last-step-data :valid? all-valid?)]]
    [step-label (dec (count steps)) current-step last-step]))

(defn- header [{:keys [index steps]}]
  [:div {:id "mt-stepper--header"}
    [step-labels index steps]
    [last-step-label index steps]])

;; ----- Header -----
;; -----------------------------------------------------------------------------

;; -----------------------------------------------------------------------------
;; ----- Body -----

(defn- step [[_ {:keys [content]}]]
  [:div {:id "mt-stepper--step-container"}
    content])

(defn- body [{:keys [current-step]}]
  [:div {:id "mt-stepper--body"}
    [step current-step]])

;; ----- Body -----
;; -----------------------------------------------------------------------------

;; -----------------------------------------------------------------------------
;; ----- Footer -----

(defn- back-button [step-index]
  (if (< 0 step-index)
     [x.elements/button {:label            "Vissza"
                         :on-click         #(r/dispatch [:stepper/back!])
                         :variant          :outlined
                         :style            {:padding "15px"}
                         :background-color :muted}]))

(defn- next-button [{:keys [] [_ {:keys [valid?]}] :current-step}]
  [x.elements/button {:label            "Következő"
                      :on-click         #(r/dispatch [:stepper/next!])
                      :disabled?        (not valid?)
                      :variant          :outlined
                      :style            {:padding "15px"}
                      :background-color :primary}])

(defn- finish-button [{:keys [steps current-step config]}]
  (let [[_ {:keys [valid? on-click]}] current-step
        disable? (if-not (nil? valid?) 
                   (not valid?)
                   (not (every? (fn [[_ {:keys [valid?]}]] (true? valid?)) (butlast steps))))]
    [x.elements/button {:disabled disable?
                        :background-color :primary
                        :on-click #(on-click)
                        :label    (get config :finish-step-label "Finish")}]))

(defn- footer [{:keys [index steps] :as view-props}]
  (let [last-step? (= (inc index) (count steps))]
    [:div {:id "mt-stepper--footer"}
      [back-button index]
      (if last-step? 
        [finish-button view-props]
        [next-button view-props])]))

;; ----- Footer -----
;; -----------------------------------------------------------------------------

(defn- stepper [config args]
  (let [step-index @(r/subscribe [:x.db/get-item [:stepper] 0])
        steps      (partition 2 args)
        view-props {:index        step-index
                    :steps        steps
                    :config       config
                    :current-step (nth steps step-index)
                    :on-finish    (:on-finish config)}]
    [:div#mt-stepper--container
      [header view-props]
      [body view-props]
      [footer view-props]]))

(defn component [config & steps]
  (assert (even? (count steps))
          (str "\n\n COMPONENTS/STEPPER FAILED!\n steps must be even key value pair!
               \n Given steps: \n" steps "
               \n"))
  (reagent/lifecycles
    {:component-did-mount #(r/dispatch [:x.db/set-item! [:stepper] 0])
     :reagent-render
     (fn [config & steps]
       [stepper config steps])}))
