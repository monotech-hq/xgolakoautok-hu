
(ns app.clients.frontend.lister.effects
    (:require [app.clients.frontend.lister.views :as lister.views]
              [re-frame.api                      :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :clients.lister/load-lister!
  [:clients.lister/render-lister!])

(r/reg-event-fx :clients.lister/render-lister!
  [:x.ui/render-surface! :clients.lister/view
                         {:content #'lister.views/view}])
