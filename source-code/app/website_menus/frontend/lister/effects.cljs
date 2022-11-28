
(ns app.website-menus.frontend.lister.effects
    (:require [app.website-menus.frontend.lister.views :as lister.views]
              [re-frame.api                            :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-menus.lister/load-lister!
  [:website-menus.lister/render-lister!])

(r/reg-event-fx :website-menus.lister/render-lister!
  [:x.ui/render-surface! :website-menus.lister/view
                         {:content #'lister.views/view}])
