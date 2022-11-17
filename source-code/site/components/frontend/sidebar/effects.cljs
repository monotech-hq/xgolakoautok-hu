
(ns site.components.frontend.sidebar.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :components.sidebar/show-sidebar!
  ; @usage
  ;  [:components.sidebar/show-sidebar!]
  (fn [{:keys [db]} _]
      {:db       (assoc-in db [:components :sidebar/meta-items :visible?] true)
       :dispatch [:x.environment/add-scroll-prohibition! ::prohibition]}))

(r/reg-event-fx :components.sidebar/hide-sidebar!
  ; @usage
  ;  [:components.sidebar/hide-sidebar!]
  (fn [{:keys [db]} _]
      {:db       (assoc-in db [:components :sidebar/meta-items :visible?] false)
       :dispatch [:x.environment/enable-scroll!]}))
