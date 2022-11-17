
(ns site.components.frontend.credits.views
    (:require [elements.api                                   :as elements]
              [random.api                                     :as random]
              [re-frame.api                                   :as r]
              [site.components.frontend.copyright-label.views :as copyright-label.views]
              [site.components.frontend.mt-logo.views         :as mt-logo.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn created-by-label
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:theme (keyword)(opt)
  ;    :light, :dark
  ;    Default: :light}
  ;
  ; @usage
  ;  [created-by-label]
  [_ {:keys [theme]}]
  [elements/label ::created-by-label
                  {:color     (case theme :dark :invert :default)
                   :content   "Created by"
                   :font-size :xs
                   :indent    {:top :xxs}}])

(defn created-by
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:theme (keyword)(opt)
  ;    :light, :dark
  ;    Default: :light}
  ;
  ; @usage
  ;  [created-by]
  [component-id component-props]
  [elements/toggle ::created-by
                   {:on-click {:fx [:x.environment/open-new-browser-tab! "https://www.monotech.hu"]}
                    :content  [:div {:style {:align-items "center" :display "flex" :flex-direction "column"}}
                                    [mt-logo.views/component component-id component-props]
                                    [created-by-label        component-id component-props]]
                    :indent   {:bottom :xxs}}])

(defn- credits
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:style (map)(opt)}
  [component-id {:keys [style] :as component-props}]
  [:div {:style style}
        [:div {:style {:display "flex" :justify-content "center"}}
              [created-by component-id component-props]]
        [copyright-label.views/component component-id component-props]])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:style (map)(opt)
  ;   :theme (keyword)(opt)
  ;    :light, :dark
  ;    Default: :light}
  ;
  ; @usage
  ;  [credits {...}]
  ;
  ; @usage
  ;  [credits :my-credits {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [credits component-id component-props]))
