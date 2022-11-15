
(ns app.contents.frontend.lister.effects
  (:require [app.contents.frontend.lister.views :as lister.views]
            [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :contents.lister/load-lister!
  [:x.ui/render-surface! :contents.lister/view
                         {:content #'lister.views/view}])
