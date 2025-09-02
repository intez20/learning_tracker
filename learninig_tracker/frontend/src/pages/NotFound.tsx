import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '../components/ui/button';
import { AlertTriangle } from 'lucide-react';

const NotFound = () => {
  return (
    <div className="flex flex-col items-center justify-center h-[70vh] text-center">
      <AlertTriangle className="h-16 w-16 text-amber-500 mb-4" />
      <h1 className="text-4xl font-bold mb-2">Page Not Found</h1>
      <p className="text-muted-foreground mb-6 max-w-md">
        The page you are looking for doesn't exist or has been moved.
      </p>
      <Link to="/">
        <Button>
          Return to Dashboard
        </Button>
      </Link>
    </div>
  );
};

export default NotFound;
