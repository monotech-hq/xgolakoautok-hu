
(ns app.home.frontend.sidebar.events
    (:require [app.home.frontend.sidebar.prototypes :as sidebar.prototypes]
              [mid-fruits.vector                    :as vector]
              [re-frame.api                         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-menu-item!
  ; @param (map) item-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :group (keyword)(opt)
  ;    Default: :other
  ;   :icon (keyword)
  ;   :icon-color (string)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :label (string)
  ;   :on-click (metamorphic-event)
  ;   :vertical-weight (integer)(opt)
  ;    Default: 0}
  [db [_ item-props]]
  (let [item-props (sidebar.prototypes/item-props-prototype item-props)]
       (update-in db [:home :sidebar/menu-items] vector/conj-item item-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:home.sidebar/add-menu-item! {:label    "My item"
;                                 :icon     :festival
;                                 :on-click [:router/go-to! "/@app-home/my-item"]}]
(r/reg-event-db :home.sidebar/add-menu-item! add-menu-item!)
